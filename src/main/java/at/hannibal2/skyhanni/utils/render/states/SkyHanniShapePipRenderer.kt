package at.hannibal2.skyhanni.utils.render.states

import at.hannibal2.skyhanni.utils.render.SkyHanniPipelineSubmit
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.state.gui.GuiRenderState

//? if >= 26.2 {
class SkyHanniShapePipRenderer : PictureInPictureRenderer<SkyHanniShapePipState>() {
//?} else {
/*class SkyHanniShapePipRenderer(
    private val legacyBufferSource: net.minecraft.client.renderer.MultiBufferSource.BufferSource,
) : PictureInPictureRenderer<SkyHanniShapePipState>(legacyBufferSource) {*/
//?}
    override fun textureIsReadyToBlit(state: SkyHanniShapePipState) = false

    //? if >= 26.2 {
    override fun renderToTexture(
        state: SkyHanniShapePipState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
    ) {
        poseStack.translate(-state.x1() / 2f, -state.y1() / 2f, 0f)
        for (shape in state.shapes) {
            val name = "skyhanni_shape_${shape.pipeline()}"
            shape.textureIdentifier()?.let { texture ->
                SkyHanniPipelineSubmit.submitTextured(
                    submitNodeCollector,
                    name,
                    shape.pipeline(),
                    texture,
                ) { consumer -> shape.buildVertices(consumer) }
            } ?: SkyHanniPipelineSubmit.submit(
                submitNodeCollector,
                name,
                shape.pipeline(),
                shape.textureSetup(),
            ) { consumer -> shape.buildVertices(consumer) }
        }
    }
    //?} else {
    /*override fun renderToTexture(state: SkyHanniShapePipState, poseStack: PoseStack) {
        SkyHanniPipelineSubmit.renderLegacy(
            legacyBufferSource,
            "skyhanni_shape",
            state.shapes.first().pipeline(),
        ) { consumer -> state.shapes.forEach { it.buildVertices(consumer) } }
    }*/
    //?}

    override fun blitTexture(state: SkyHanniShapePipState, guiRenderState: GuiRenderState) {
        super.blitTexture(state, guiRenderState)
    }

    override fun getRenderStateClass() = SkyHanniShapePipState::class.java
    override fun getTextureLabel() = "skyhanni_shape"
}
