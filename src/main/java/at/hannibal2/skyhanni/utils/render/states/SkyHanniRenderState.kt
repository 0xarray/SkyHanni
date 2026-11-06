package at.hannibal2.skyhanni.utils.render.states

import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import net.minecraft.resources.Identifier

interface SkyHanniRenderState : GuiElementRenderState {
    fun textureIdentifier(): Identifier? = null
}
