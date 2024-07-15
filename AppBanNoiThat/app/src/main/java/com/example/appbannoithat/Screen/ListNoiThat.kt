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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.nav.SortState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNoiThat(navController: NavHostController, viewModel: ViewModel, id: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listNTs = viewModel.noiThats.observeAsState()
    val lstNTs = listNTs.value ?: emptyList()

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier
            .background(color = Color.White)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {

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
        PageNTs(viewModel, lstNTs, it, id, navController)
    }
}

@ExperimentalMaterial3Api
@Composable
fun PageNTs(viewModel: ViewModel, lstNTs: List<NoiThat>, it: PaddingValues, id: String, navController: NavHostController) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Loai NT chua dien"
                )
                Text(
                    text = "tong sp cua loai"
                )
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
            ) {
                Text(
                    text = "Biens loc trang thai",
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
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
            .padding(16.dp)
    ) {
        Text(
            text = "Mặc định",
            modifier = Modifier.clickable {
                viewModel.setSortState(SortState.DEFAULT)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "moi nhat",
            modifier = Modifier.clickable {
                viewModel.getnew(id)
                viewModel.setSortState(SortState.NEWEST)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "gia tang",
            modifier = Modifier.clickable {
                viewModel.gettang(id)
                viewModel.setSortState(SortState.PRICE_ASCENDING)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "gia giam",
            modifier = Modifier.clickable {
                viewModel.getgiam(id)
                viewModel.setSortState(SortState.PRICE_DESCENDING)
                onClose()
            },
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Ban chay",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Close")
        }
    }
}

@Composable
fun ItemNoiThat(Item: NoiThat, onClick : () -> Unit) {

    Column(
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = rememberImagePainter(Item.hinh_anh[0]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "${Item.ten_noi_that}"
        )
        Text(
            text = "${Item.gia}"
        )

    }
}