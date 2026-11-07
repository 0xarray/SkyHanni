package at.hannibal2.skyhanni.features.garden.pests.stereo

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.events.InventoryUpdatedEvent
import at.hannibal2.skyhanni.events.render.gui.ReplaceItemEvent
import at.hannibal2.skyhanni.features.garden.pests.PestApi
import at.hannibal2.skyhanni.features.garden.pests.PestType
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.DelayedRun
import at.hannibal2.skyhanni.utils.ItemUtils.addEnchantGlint
import at.hannibal2.skyhanni.utils.ItemUtils.getInternalNameOrNull
import at.hannibal2.skyhanni.utils.ItemUtils.getLoreComponent
import at.hannibal2.skyhanni.utils.ItemUtils.setLore
import at.hannibal2.skyhanni.utils.RegexUtils.anyMatchesComponent
import at.hannibal2.skyhanni.utils.SafeItemStack
import at.hannibal2.skyhanni.utils.collection.TimeLimitedCache
import at.hannibal2.skyhanni.utils.compat.setCustomItemName
import kotlin.time.Duration.Companion.minutes

@SkyHanniModule
object StereoHarmonyDiscReplacer {

    private val config get() = PestApi.config.stereoHarmony

    private val iconCache = TimeLimitedCache<Int, SafeItemStack>(10.minutes,)

    @HandleEvent
    private fun onInventoryUpdated(event: InventoryUpdatedEvent) {
        if (!PestApi.stereoInventory.isInside()) return

        DelayedRun.runOrNextTick {
            iconCache.clear()
            for (slot in REPLACEMENT_SLOTS) {
                event.inventoryItems[slot]?.let { item ->
                    createReplacement(item)?.let { iconCache[slot] = it }
                }
            }
        }
    }

    @HandleEvent
    private fun replaceItem(event: ReplaceItemEvent) {
        if (!config.replaceMenuIcons) return
        if (!PestApi.stereoInventory.isInside()) return
        if (event.slot !in REPLACEMENT_SLOTS) return
        val replacementStack = iconCache[event.slot] ?: return
        event.replace(replacementStack)
    }

    private fun createReplacement(item: SafeItemStack): SafeItemStack? {
        val internalName = item.getInternalNameOrNull() ?: return null
        val vinylType = VinylType.getByInternalNameOrNull(internalName) ?: return null
        val cropType = PestType.getByVinylOrNull(vinylType)?.crop ?: return null
        val lore = item.getLoreComponent()
        val isActiveVinyl = PestApi.stereoPlayingItemPattern.anyMatchesComponent(lore)
        return cropType.icon.copy().apply {
            if (isActiveVinyl) addEnchantGlint()
            setLore(lore)
            setCustomItemName(item.hoverName)
        }
    }

    private val REPLACEMENT_SLOTS = buildSet {
        addAll(11..15)
        addAll(20..24)
        addAll(30..32)
    }
}
