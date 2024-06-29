package com.example.appbannoithat.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun ThuMuc(it: PaddingValues, viewModel: ViewModel, navController: NavController) {
    val gridState = rememberLazyStaggeredGridState()

    val danhMuc = viewModel.danhmuc.observeAsState()
    val danhmucs = danhMuc.value ?: emptyList()

    val listLNT = viewModel.loaiNT.observeAsState()
    val LNTs = listLNT.value ?: emptyList()

    val selectedDanhMucId = remember { mutableStateOf<String?>(null) }

    Row {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(it)
        ) {
            items(danhmucs.size) {
                ItemDanhMuc(
                    danhmucs[it],
                    onClickItem = {
                        selectedDanhMucId.value = danhmucs[it]._id
                        viewModel.getLoaiNT(danhmucs[it]._id)
                    }
                )
            }
        }
        if (LNTs.size === 0) {
            Column(
                modifier = Modifier
                    .padding(top = 200.dp, bottom = 100.dp)
                    .weight(2.5f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Rất tiếc, hiện không có sản phẩm nào",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 100.dp)
                    .weight(2.5f),
                columns = StaggeredGridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                contentPadding = PaddingValues(8.dp)
            ) {
                items(LNTs.size) { index ->
                    ItemLoaiNT(
                        Item = LNTs[index],
                        onListNoiThat = {
                            viewModel.getNoiThats(LNTs[index]._id)
                            navController.navigate("listNoiThat/${LNTs[index]._id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemDanhMuc(danhM: DanhMuc, onClickItem: () -> Unit) {
    Log.d("Danh Muc", "${danhM.ten_danh_muc}")
    Column(
        modifier = Modifier.clickable { onClickItem() }
    ) {
        Text(
            text = "${danhM.ten_danh_muc}"
        )
        Text(
            text = "So luong chua co"
        )
    }
}

//, onItemNoiThat: () -> Unit
@Composable
fun ItemLoaiNT(Item: LoaiNoiThat, onListNoiThat: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                onClick = onListNoiThat
            ),
    ) {
        Column(
            modifier = Modifier
                .height(130.dp)
                .width(100.dp)
        ) {
            Image(
                painter = rememberImagePainter(Item.img),
                contentDescription = null,
                modifier = Modifier
                    .weight(3f)
                    .height(100.dp)
                    .width(100.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "${Item.ten_loai}",
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .align(Alignment.CenterHorizontally),
                maxLines = 2
            )
        }
    }
}
