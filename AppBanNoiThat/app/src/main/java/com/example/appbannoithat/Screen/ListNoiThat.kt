package com.example.appbannoithat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.nav.SortState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNoiThat(navController: NavHostController, viewModel: ViewModel, id: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listNTs = viewModel.noiThats.observeAsState()
    val lstNTs = listNTs.value ?: emptyList()

    val loaiNTs = viewModel.allloainoithat.observeAsState()
    val loaiNT = loaiNTs.value?.firstOrNull { x ->
        x._id == id
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier
            .background(color = Color.White)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if (lstNTs.isEmpty()) {
                        Text(
                            text = "Danh mục đang trống"
                        )
                    } else {
                        Text(
                            text = "${loaiNT?.ten_loai}"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        navController.popBackStack()
                                    }
                                ),
                            tint = Color.Black,
                        )
                    }
                }
            )
        }
    ) {
        PageNTs(viewModel, lstNTs, it, id, navController, loaiNT)
    }
}

@ExperimentalMaterial3Api
@Composable
fun PageNTs(
    viewModel: ViewModel,
    lstNTs: List<NoiThat>,
    it: PaddingValues,
    id: String,
    navController: NavHostController,
    loaiNT: LoaiNoiThat?,
) {
    val gridState = rememberLazyStaggeredGridState()
    var isDialog by remember { mutableStateOf(false) }

    val currentSortState by viewModel.currentSortState.observeAsState(SortState.DEFAULT)
    val productsToDisplay = when (currentSortState) {
        SortState.NEWEST -> viewModel.NewNoiThatCT.observeAsState(emptyList()).value
        SortState.PRICE_ASCENDING -> viewModel.TangNoiThatCT.observeAsState(emptyList()).value
        SortState.PRICE_DESCENDING -> viewModel.GiamNoiThatCT.observeAsState(emptyList()).value
        else -> lstNTs
    }


    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (lstNTs.isEmpty()) {
                    Text(
                        text = "Danh mục đang trống"
                    )
                } else {
                    Text(
                        text = "${loaiNT?.ten_loai}",
                        fontSize = 15.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        isDialog = true
                    }
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tất cả sản phẩm",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(10.dp),
            columns = StaggeredGridCells.Fixed(2),
            state = gridState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(productsToDisplay.size) { index ->
                ItemNoiThat(Item = productsToDisplay[index], onClick = {
                    viewModel.getNoiThatCT(productsToDisplay[index]._id)
                    navController.navigate("noiThat")
                })
            }
        }
    }

    if (isDialog) {
        if (lstNTs.size > 1 || lstNTs.size <= 1) {
            ModalBottomSheet(
                onDismissRequest = { isDialog = false }
            ) {
                BottomSheetContent(viewModel, onClose = { isDialog = false }, id)
            }
        }
    }
}

@Composable
fun BottomSheetContent(viewModel: ViewModel, onClose: () -> Unit, id: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp),
    ) {
        Text(
            text = "Tất cả sản phầm",
            fontSize = 15.sp,
            modifier = Modifier.clickable {
                viewModel.setSortState(SortState.DEFAULT)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Hàng mới về trong 30 ngày gần đây",
            fontSize = 15.sp,
            modifier = Modifier.clickable {
                viewModel.getnew(id)
                viewModel.setSortState(SortState.NEWEST)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Giá từ thấp lên cao",
            fontSize = 15.sp,
            modifier = Modifier.clickable {
                viewModel.gettang(id)
                viewModel.setSortState(SortState.PRICE_ASCENDING)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Giá từ cao xuống thấp",
            fontSize = 15.sp,
            modifier = Modifier.clickable {
                viewModel.getgiam(id)
                viewModel.setSortState(SortState.PRICE_DESCENDING)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Bán chạy",
            fontSize = 15.sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun ItemNoiThat(Item: NoiThat, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .width(150.dp)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(Item.hinh_anh[0]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "${Item.ten_noi_that}",
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp)
        )

        Text(
            text = "${Item.gia} đ",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp)
        )

        LazyRow{
            items(Item.hinh_anh.size) { index ->
                Image(
                    painter = rememberImagePainter(Item.hinh_anh[index]),
                    contentDescription = null,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .padding(start = 7.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}