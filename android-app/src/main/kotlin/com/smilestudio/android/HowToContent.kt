package com.smilestudio.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.smilestudio.core.ParseResult
import com.smilestudio.core.SmilesParser
import com.smilestudio.ui.MoleculeCanvas

private data class SmilesExample(val smiles: String, val title: String, val description: String)

private val EXAMPLES = listOf(
    SmilesExample("C", "原子", "元素記号をそのまま書きます。「C」は炭素（メタン）です。"),
    SmilesExample("CCO", "単結合の鎖", "原子を並べるだけで単結合になります。「CCO」はエタノールの骨格です。"),
    SmilesExample("C=C", "二重結合", "「=」で二重結合を表します。"),
    SmilesExample("C#C", "三重結合", "「#」で三重結合を表します。"),
    SmilesExample("C1CCCCC1", "環（脂肪族）", "同じ数字を原子の直後に書くと、その2つの原子が結合して環になります。「C1CCCCC1」はシクロヘキサンです。"),
    SmilesExample("c1ccccc1", "芳香環", "小文字の元素記号は芳香族を表します。「c1ccccc1」はベンゼンです。"),
)

@Composable
fun HowToContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(
                "SMILES記法の基本",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Text(
                "SMILESは、化学構造をテキストの1行で表す記法です。代表的な書き方を紹介します。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }
        items(EXAMPLES) { example -> ExampleCard(example, modifier = Modifier.padding(bottom = 16.dp)) }
    }
}

@Composable
private fun ExampleCard(example: SmilesExample, modifier: Modifier = Modifier) {
    val molecule = (SmilesParser.parse(example.smiles) as? ParseResult.Success)?.molecule

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(example.title, style = MaterialTheme.typography.titleMedium)
            Text(
                example.smiles,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            Text(example.description, style = MaterialTheme.typography.bodyMedium)
            MoleculeCanvas(
                molecule = molecule,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 8.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp)),
            )
        }
    }
}
