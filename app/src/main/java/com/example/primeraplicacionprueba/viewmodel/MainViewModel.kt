package com.example.primeraplicacionprueba.viewmodel

class MainViewModel(
    val placesViewModel: PlacesViewModel,
    val reviewsViewModel: ReviewViewModel,
    val usersViewModel: UsersViewModel,
    val achievementViewModel: AchievementViewModel,
    val commentsViewModel: CommentsViewModel
) {
    init {
        // Set up cross-ViewModel dependencies
        placesViewModel.setReviewViewModel(reviewsViewModel)
    }
}
