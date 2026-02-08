package com.app.makeadonation.ngocategories.presentation

import com.app.makeadonation.Utils
import com.app.makeadonation.R
import com.app.makeadonation.common.BaseEvent
import com.app.makeadonation.common.CoroutinesTestRule
import com.app.makeadonation.common.TextProvider
import com.app.makeadonation.ngocategories.domain.entity.NgoCategory
import com.app.makeadonation.ngocategories.domain.usecase.NgoCategoriesUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class NGOCategoriesViewModelTest : KoinTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val useCase: NgoCategoriesUseCase = mockk(relaxed = true)
    private val textProvider: TextProvider = mockk(relaxed = true)

    private val viewModel: NGOCategoriesViewModel by inject()

    val categories = Utils.retrieveObjectFromFile<List<NgoCategory>>("ong_categories.json")

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                single<TextProvider> { textProvider }
                single<NgoCategoriesUseCase> { useCase }
                factory { NGOCategoriesViewModel(get(),get()) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when loading NGO categories, check list size`() = runTest {
        coEvery { useCase.retrieveCategories() } returns flowOf(categories)

        viewModel.init()
        advanceUntilIdle()

        val showLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(showLoadingEvent is BaseEvent.ShowLoading)

        val categoriesEvent = viewModel.ngoCategoriesChannel.receive()

        assertTrue(categoriesEvent is NGOCategoriesEvent.Categories)

        (categoriesEvent as NGOCategoriesEvent.Categories).run {
            assertEquals(4, categoriesEvent.categories.size)
        }

        val hideLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(hideLoadingEvent is BaseEvent.HideLoading)
    }

    @Test
    fun `when loading NGO categories, check 2 ngo categories`() = runTest {
        val categoriesToCheck = listOf(
            NgoCategory("0", "forest", "Ambiente"),
            NgoCategory("1", "education", "Educação")
        )

        coEvery { useCase.retrieveCategories() } returns flowOf(categories)

        viewModel.init()
        advanceUntilIdle()

        val showLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(showLoadingEvent is BaseEvent.ShowLoading)

        val categoriesEvent = viewModel.ngoCategoriesChannel.receive()

        assertTrue(categoriesEvent is NGOCategoriesEvent.Categories)

        (categoriesEvent as NGOCategoriesEvent.Categories).run {
            assertEquals(categoriesToCheck[0], categoriesEvent.categories[0])
            assertEquals(categoriesToCheck[1], categoriesEvent.categories[1])
        }


        val hideLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(hideLoadingEvent is BaseEvent.HideLoading)
    }

    @Test
    fun `when loading NGO categories, handles exception`() = runTest {
        every { textProvider.getText(R.string.warning) } returns "Aviso"
        every { textProvider.getText(R.string.donate_categories_empty_list_error) } returns "Não foi possível obter a lista de categorias"

        coEvery { useCase.retrieveCategories() } returns flow { throw Exception() }

        viewModel.init()
        advanceUntilIdle()

        val showLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(showLoadingEvent is BaseEvent.ShowLoading)

        val emptyCategoriesEvent = viewModel.ngoCategoriesChannel.receive()

        assertTrue(emptyCategoriesEvent is NGOCategoriesEvent.EmptyCategories)

        (emptyCategoriesEvent as NGOCategoriesEvent.EmptyCategories).run {
            assertEquals(title, "Aviso")
            assertEquals(description, "Não foi possível obter a lista de categorias")
        }


        val hideLoadingEvent = viewModel.ngoCategoriesChannel.receive()
        assertTrue(hideLoadingEvent is BaseEvent.HideLoading)
    }
}
