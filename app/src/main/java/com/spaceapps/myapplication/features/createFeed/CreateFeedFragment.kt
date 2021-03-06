package com.spaceapps.myapplication.features.createFeed

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFeedFragment : ComposableFragment() {

    private val vm by viewModels<CreateFeedViewModel>()

    @Composable
    override fun Content() = CreateFeedScreen(vm)
}
