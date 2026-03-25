package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.ControlParam
import com.gearboard.domain.model.Effect
import com.gearboard.domain.model.Pedal
import com.gearboard.domain.model.TapButton
import com.gearboard.domain.model.ToggleButton
import com.gearboard.ui.theme.GearBoardColors

// Predefined pedal templates
private val pedalTemplates = listOf(
    Pedal(
        name = "Overdrive",
        type = "Distortion",
        controls = listOf(
            ControlParam("od_drive", "Drive", ccNumber = 1),
            ControlParam("od_tone", "Tone", ccNumber = 2),
            ControlParam("od_level", "Level", ccNumber = 3)
        ),
        toggleButtons = listOf(ToggleButton("od_boost", "Boost", ccNumber = 4))
    ),
    Pedal(
        name = "Delay",
        type = "Time",
        controls = listOf(
            ControlParam("dl_time", "Time", unit = "ms", maxValue = 2000f, ccNumber = 5),
            ControlParam("dl_feedback", "Feedback", unit = "%", ccNumber = 6),
            ControlParam("dl_mix", "Mix", unit = "%", ccNumber = 7)
        ),
        tapButtons = listOf(TapButton("dl_tap", "TAP", ccNumber = 8))
    ),
    Pedal(
        name = "Reverb",
        type = "Ambient",
        controls = listOf(
            ControlParam("rv_decay", "Decay", ccNumber = 9),
            ControlParam("rv_mix", "Mix", unit = "%", ccNumber = 10),
            ControlParam("rv_tone", "Tone", ccNumber = 11)
        )
    ),
    Pedal(
        name = "Chorus",
        type = "Modulation",
        controls = listOf(
            ControlParam("ch_rate", "Rate", unit = "Hz", maxValue = 10f, ccNumber = 12),
            ControlParam("ch_depth", "Depth", unit = "%", ccNumber = 13),
            ControlParam("ch_mix", "Mix", unit = "%", ccNumber = 14)
        )
    ),
    Pedal(
        name = "Compressor",
        type = "Dynamics",
        controls = listOf(
            ControlParam("cmp_thresh", "Thresh", unit = "dB", ccNumber = 15),
            ControlParam("cmp_ratio", "Ratio", ccNumber = 16),
            ControlParam("cmp_gain", "Gain", unit = "dB", ccNumber = 17)
        )
    ),
    Pedal(
        name = "Wah",
        type = "Filter",
        controls = listOf(
            ControlParam("wah_pos", "Position", ccNumber = 18),
            ControlParam("wah_range", "Range", ccNumber = 19),
            ControlParam("wah_q", "Q", ccNumber = 20)
        )
    )
)

// Predefined effect templates
private val effectTemplates = listOf(
    Effect(
        name = "Graphic EQ",
        type = "EQ",
        controls = listOf(
            ControlParam("eq_80", "80Hz", unit = "dB", minValue = -12f, maxValue = 12f, ccNumber = 30),
            ControlParam("eq_250", "250Hz", unit = "dB", minValue = -12f, maxValue = 12f, ccNumber = 31),
            ControlParam("eq_800", "800Hz", unit = "dB", minValue = -12f, maxValue = 12f, ccNumber = 32),
            ControlParam("eq_2k5", "2.5kHz", unit = "dB", minValue = -12f, maxValue = 12f, ccNumber = 33),
            ControlParam("eq_8k", "8kHz", unit = "dB", minValue = -12f, maxValue = 12f, ccNumber = 34)
        )
    ),
    Effect(
        name = "Noise Gate",
        type = "Dynamics",
        controls = listOf(
            ControlParam("ng_thresh", "Threshold", unit = "dB", ccNumber = 35),
            ControlParam("ng_decay", "Decay", unit = "ms", ccNumber = 36)
        )
    ),
    Effect(
        name = "Tremolo",
        type = "Modulation",
        controls = listOf(
            ControlParam("tr_speed", "Speed", unit = "Hz", maxValue = 10f, ccNumber = 37),
            ControlParam("tr_depth", "Depth", unit = "%", ccNumber = 38),
            ControlParam("tr_wave", "Wave", ccNumber = 39)
        )
    ),
    Effect(
        name = "Phaser",
        type = "Modulation",
        controls = listOf(
            ControlParam("ph_rate", "Rate", unit = "Hz", maxValue = 10f, ccNumber = 40),
            ControlParam("ph_depth", "Depth", unit = "%", ccNumber = 41),
            ControlParam("ph_feedback", "Feedback", unit = "%", ccNumber = 42)
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPedalSheet(
    onAddPedal: (Pedal) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = GearBoardColors.Surface,
        contentColor = GearBoardColors.TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "ADD PEDAL",
                color = GearBoardColors.Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            pedalTemplates.forEach { template ->
                TemplateItem(
                    name = template.name,
                    type = template.type,
                    controlCount = template.controls.size,
                    onClick = { onAddPedal(template.copy(id = java.util.UUID.randomUUID().toString())) }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEffectSheet(
    onAddEffect: (Effect) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = GearBoardColors.Surface,
        contentColor = GearBoardColors.TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "ADD EFFECT",
                color = GearBoardColors.Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            effectTemplates.forEach { template ->
                TemplateItem(
                    name = template.name,
                    type = template.type,
                    controlCount = template.controls.size,
                    onClick = { onAddEffect(template.copy(id = java.util.UUID.randomUUID().toString())) }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TemplateItem(
    name: String,
    type: String,
    controlCount: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.SurfaceVariant)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = name.uppercase(),
            color = GearBoardColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        Text(
            text = "$type • $controlCount controls",
            color = GearBoardColors.TextDisabled,
            fontSize = 11.sp
        )
    }
}
