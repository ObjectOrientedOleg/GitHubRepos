package com.objectorientedoleg.githubrepos.feature.topstarredrepos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.objectorientedoleg.githubrepos.R
import com.objectorientedoleg.githubrepos.ui.shimmer
import com.objectorientedoleg.githubrepos.ui.theme.GitHubReposTheme

@Composable
fun TopStarredReposRoute(
    modifier: Modifier = Modifier,
    viewModel: TopStarredReposViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TopStarredReposScreen(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
private fun TopStarredReposScreen(
    uiState: TopStarredReposUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopStarredReposTopBar() }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when (uiState) {
                TopStarredReposUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                is TopStarredReposUiState.Success -> TopStarredReposContent(
                    modifier = Modifier.matchParentSize(),
                    items = uiState.items
                )

                TopStarredReposUiState.Error -> TopStarredReposError(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopStarredReposTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.app_name))
        }
    )
}

@Composable
private fun TopStarredReposError(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.something_went_wrong),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun TopStarredReposContent(
    items: RepositoryItems,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        topStarredReposItems(
            itemModifier = Modifier.fillMaxWidth(),
            items = items,
        )
    }
}

private fun LazyListScope.topStarredReposItems(
    items: List<RepositoryItem>,
    itemModifier: Modifier = Modifier
) {
    items(
        items = items,
        key = { it.id }
    ) { item ->
        RepositoryItem(
            modifier = itemModifier,
            item = item
        )
    }
}

@Composable
private fun RepositoryItem(
    item: RepositoryItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                model = item.avatarUrl,
                placeholder = ColorPainter(Color.Gray),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = item.rank,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        if (item.description != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.language != null) {
                Text(
                    text = item.language,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(4.dp))
            }
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null
            )
            Text(
                text = item.starCount,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        when (item.topContributor) {
            TopContributor.Loading, is TopContributor.Success -> {
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.top_contributor),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.width(4.dp))
                    if (item.topContributor is TopContributor.Loading) {
                        Box(
                            modifier = Modifier
                                .size(
                                    width = (50..100).random().dp,
                                    height = MaterialTheme.typography.bodyLarge.fontSize.value.dp
                                )
                                .background(
                                    color = Color.Gray,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .shimmer()
                        )
                    } else if (item.topContributor is TopContributor.Success) {
                        Text(
                            text = item.topContributor.topContributor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            TopContributor.Error -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopStarredReposScreenPreview(
    @PreviewParameter(UiStatePreviewParameterProvider::class) uiState: TopStarredReposUiState
) {
    GitHubReposTheme {
        TopStarredReposScreen(uiState = uiState)
    }
}