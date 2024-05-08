package com.objectorientedoleg.githubrepos.feature.topstarredrepos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.objectorientedoleg.githubrepos.data.GitHubReposRepository
import com.objectorientedoleg.githubrepos.model.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopStarredReposViewModel @Inject constructor(
    private val gitHubReposRepository: GitHubReposRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TopStarredReposUiState>(TopStarredReposUiState.Loading)
    val uiState: StateFlow<TopStarredReposUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gitHubReposRepository.getTopHundredStarredRepos().collect { repositories ->
                if (repositories.isNotEmpty()) {
                    handleSuccess(repositories)
                } else {
                    handleError()
                }
            }
        }
    }

    private suspend fun handleSuccess(repositories: List<GitHubRepository>) {
        val items = repositories
            .mapIndexed { index, repository ->
                repository.toRepositoryItem(index.inc().toString())
            }
            .let { RepositoryItems(it) }
        _uiState.update {
            TopStarredReposUiState.Success(items)
        }
        findTopContributors(repositories)
    }

    private suspend fun findTopContributors(repositories: List<GitHubRepository>) {
        coroutineScope {
            val items = repositories
                .mapIndexed { index, repository ->
                    async {
                        val topContributor = gitHubReposRepository
                            .getTopContributor(repository)
                            .first()
                            ?.let { TopContributor.Success(it.name) }
                            ?: TopContributor.Error
                        repository.toRepositoryItem(index.inc().toString(), topContributor)
                    }
                }
                .awaitAll()
                .let { RepositoryItems(it) }
            _uiState.update {
                TopStarredReposUiState.Success(items)
            }
        }
    }

    private fun handleError() {
        _uiState.update { currentState ->
            if (currentState is TopStarredReposUiState.Loading) {
                TopStarredReposUiState.Error
            } else {
                currentState
            }
        }
    }
}

private fun GitHubRepository.toRepositoryItem(
    rank: String,
    topContributor: TopContributor = TopContributor.Loading
) = RepositoryItem(
    id = id,
    name = fullName,
    description = description,
    language = language,
    avatarUrl = owner.avatarUrl,
    starCount = starCount.toString(),
    rank = rank,
    topContributor = topContributor,
)