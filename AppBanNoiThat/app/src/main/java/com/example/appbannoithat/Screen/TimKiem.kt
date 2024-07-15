package com.example.appbannoithat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.ViewModel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimKiem(navController: NavHostController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(horizontal = 8.dp)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            BasicTextField(
                                value = query,
                                onValueChange = {
                                    query = it
                                    viewModel.searchNoiThat(it)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)
                            )
                            if (query.isEmpty()) {
                                Text(
                                    "Nhập từ khóa tìm kiếm",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding( start = 10.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues

        ) {
            items(searchResults.size) { noiThat ->
                SearchResultItem(
                    searchResults[noiThat],
                    itemDetail = {
                        viewModel.getNoiThatCT(searchResults[noiThat]._id)
                        navController.navigate("noiThat")
                    }
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    noiThat: NoiThat,
    itemDetail : () -> Unit
) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .clickable(
                onClick = itemDetail
            )
    ) {
        Image(
            painter = rememberImagePainter(noiThat.hinh_anh[0]),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp),
                text = noiThat.ten_noi_that,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Số lượng: ${noiThat.so_luong}",
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
            Text(
                text = "Giá: ${noiThat.gia} đ",
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}