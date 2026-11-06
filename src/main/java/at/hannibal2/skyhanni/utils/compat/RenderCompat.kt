package at.hannibal2.skyhanni.utils.compat

import com.mojang.renderpearl.api.pipeline.RenderPipeline

//~ if >= 26.4 'renderer.RenderPipelines' -> 'gui.GuiPipeline'
import net.minecraft.client.gui.GuiPipeline

object RenderCompat {
    /**
     * The depth of an empty render target. 26.2 renders with a reversed depth range,
     * so "nothing drawn" is 0 rather than 1.
     */
    //~ if < 26.2 '0.0' -> '1.0'
    const val CLEAR_DEPTH = 0.0

    //~ if >= 26.4 'RenderPipelines.GUI_TEXTURED' -> 'GuiPipeline.TEXTURED'
    fun getMinecraftGuiTextured(): RenderPipeline = GuiPipeline.TEXTURED
}
