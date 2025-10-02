package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.ui.applevel.CustomColours

class MainFlowWithNavBarBuilder private constructor() {
    data class Builder(val context: Context,
                       val viewModel: ScreenFlowViewModel,
                       val customColours: CustomColours) {
        private var customItemText: CustomItemText? = null

        private lateinit var onIconBack: () -> Unit
        private lateinit var menuItems: @Composable () -> Unit
        private lateinit var mainContent: @Composable () -> Unit

        fun setNavBarTitle(customItemText: CustomItemText) = apply {
            this.customItemText = customItemText
        }

        fun onIconBack(onIconBack: () -> Unit) = apply {
            this.onIconBack = onIconBack
        }

        fun menuItems(menuItems: @Composable () -> Unit) = apply {
            this.menuItems = menuItems
        }

        fun setContent(content: @Composable () -> Unit) = apply {
            this.mainContent = content
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Build() {
            val expanded = remember { mutableStateOf(false) }
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = customColours.secondary,
                            titleContentColor = customColours.primary
                        ),
                        title = {
                            customItemText?.let {
                                Text(text = it.text,
                                     fontSize = it.fontSize,
                                     color = it.color,
                                     fontStyle = it.fontStyle)
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onIconBack()
                            }) {
                                Icon(
                                    tint = customColours.primary,
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = context.getString(R.string.back_arrow_content_desc)
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                expanded.value = !expanded.value
                            }) {
                                Icon(
                                    tint = customColours.primary,
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = context.getString(R.string.kebab_menu_content_desc)
                                )
                                DropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false }
                                ) {
                                    menuItems()
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
            ) { innerPadding ->
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .background(customColours.background)
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                        mainContent()
                }
            }
        }
    }
}