package com.example.appbannoithat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.ShowHI
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun Home(it: PaddingValues, viewModel: ViewModel, navController: NavController) {
    viewModel.getAllloaiNoiThat()

    val allloainoithat = viewModel.allloainoithat.observeAsState()
    val all = allloainoithat.value ?: emptyList()

    LazyColumn (
        modifier = Modifier.padding(it)
    ){
      item {
          ShowHI(viewModel)
      }

      item{
          Text(
              text = "BẠN ĐANG TÌM KIẾM PHONG CÁCH MỚI CHO NGÔI NHÀ CỦA MÌNH?",
              style = MaterialTheme.typography.titleLarge.copy(
                  fontSize = 18.sp,
                  letterSpacing = 0.1.em,
                  textAlign = TextAlign.Center
              ),
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 20.dp),
              maxLines = 2,
          )
      }

      item{
          Text(
              text = "Bộ sưu tập của chúng tôi mang đến đa dạng phong cách, từ tối giản hiện đại đến thanh lịch cổ điển, giúp bạn dễ dàng tìm thấy những món đồ hoàn hảo để tô điểm cho ngôi nhà. Cho dù bạn cần một chiếc ghế sofa sang trọng cho phòng khách, bàn ăn thanh lịch cho những bữa tiệc hay chiếc giường ấm cúng cho giấc ngủ ngon, nội thất của chúng tôi đều được chế tác tỉ mỉ từ vật liệu cao cấp, đảm bảo độ bền đẹp và tinh tế.",
              modifier = Modifier
                  .padding(22.dp)
                  .fillMaxWidth(),
              textAlign = TextAlign.Center
          )

      }
//       item{
//           Text(
//               text = "SẢN PHẨM NỔI BẬT",
//               style = MaterialTheme.typography.titleLarge.copy(
//                   fontSize = 18.sp,
//                   letterSpacing = 0.1.em,
//                   textAlign = TextAlign.Center
//               ),
//               modifier = Modifier
//                   .fillMaxWidth()
//                   .padding(top = 20.dp)
//           )
//       }
//
//       item{
//           Text(
//               text = "Nhanh tay sở hữu sản phẩm chất lượng với giá `hời`! Mua ngay để không gian sống của bạn thêm trọn vẹn.",
//               modifier = Modifier
//                   .padding(22.dp)
//                   .fillMaxWidth(),
//               textAlign = TextAlign.Center
//           )
//       }

        item{
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(all.size) { index ->
                    itemLoaiNoiThat(loaiNoiThat = all[index], onClick = {
                        viewModel.getNoiThats(all[index]._id)
                        navController.navigate("listNoiThat/${all[index]._id}")
                    })
                }
            }
        }
    }
}

@Composable
fun itemLoaiNoiThat(loaiNoiThat: LoaiNoiThat, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .width(150.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberImagePainter(loaiNoiThat.img),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "${loaiNoiThat.ten_loai}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            )
    }
}