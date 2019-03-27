package com.proact.poject.serku.proact.di

import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.RequestViewModel
import com.proact.poject.serku.proact.viewmodels.UserViewModel
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module

val viewModelsModule = module {
    viewModel<UserViewModel>()
    viewModel<ProjectViewModel>()
    viewModel<RequestViewModel>()
}