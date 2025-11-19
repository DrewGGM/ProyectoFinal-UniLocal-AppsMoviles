package com.example.primeraplicacionprueba.viewmodel

class MainViewModel(
    val placesViewModel: PlacesViewModel,
    val reviewsViewModel: ReviewViewModel,
    val usersViewModel: UsersViewModel,
    val achievementViewModel: AchievementViewModel,
    val commentsViewModel: CommentsViewModel,
    val reportViewModel: ReportViewModel
) {
    init {
        // Set up cross-ViewModel dependencies
        placesViewModel.setReviewViewModel(reviewsViewModel)
        placesViewModel.setUsersViewModel(usersViewModel)
        reviewsViewModel.setUsersViewModel(usersViewModel)
    }
}
