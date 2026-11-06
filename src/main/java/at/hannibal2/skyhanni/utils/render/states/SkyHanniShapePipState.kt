package at.hannibal2.skyhanni.utils.render.states

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState
import org.joml.Matrix3x2f

/**
 * Shapes retain their screen-space coordinates while being rendered by the
 * native PIP feature pass. A full-screen target keeps those coordinates intact.
 */
class SkyHanniShapePipState : PictureInPictureRenderState {
    val shapes = ArrayList<SkyHanniRenderState>()

    override fun x0() = 0
    override fun y0() = 0
    override fun x1() = Minecraft.getInstance().window.guiScaledWidth
    override fun y1() = Minecraft.getInstance().window.guiScaledHeight
    override fun scale() = 1f
    override fun pose() = Matrix3x2f()
    override fun scissorArea(): ScreenRectangle? = shapes.firstOrNull()?.scissorArea()
    override fun bounds() = ScreenRectangle(0, 0, x1(), y1())
}
