package at.hannibal2.skyhanni.utils.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.SubmitNodeCollector;
//? if < 26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*/
//?}
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

/**
 * Small version bridge for custom GUI pipeline submissions.
 *
 * Minecraft's built-in custom feature renderer consumes a RenderType on both
 * supported versions; only the collector method differs between them.
 */
public final class SkyHanniPipelineSubmit {
    private SkyHanniPipelineSubmit() {}

    public static void submit(
        SubmitNodeCollector collector,
        String name,
        RenderPipeline pipeline,
        TextureSetup textures,
        Consumer<VertexConsumer> vertices
    ) {
        RenderSetup.RenderSetupBuilder setup = RenderSetup.builder(pipeline);
        // TextureSetup is already resolved by the PIP renderer. The texture
        // binding is added by the textured overload below.
        submit(collector, RenderType.create(name, setup.createRenderSetup()), vertices);
    }

    public static void submitTextured(
        SubmitNodeCollector collector,
        String name,
        RenderPipeline pipeline,
        Identifier texture,
        Consumer<VertexConsumer> vertices
    ) {
        RenderSetup setup = RenderSetup.builder(pipeline)
            .withTexture("Sampler0", texture)
            .createRenderSetup();
        submit(collector, RenderType.create(name, setup), vertices);
    }

    private static void submit(
        SubmitNodeCollector collector,
        RenderType renderType,
        Consumer<VertexConsumer> vertices
    ) {
        //? if >= 26.2 {
        collector.submitCustomGeometry(new PoseStack(), renderType, (pose, consumer) -> vertices.accept(consumer));
        //?} else {
        /*collector.submitCustomGeometry(new PoseStack(), renderType, (pose, consumer) -> vertices.accept(consumer));*/
        //?}
    }

    //? if < 26.2 {
    /*public static void renderLegacy(
        MultiBufferSource.BufferSource bufferSource,
        String name,
        RenderPipeline pipeline,
        Consumer<VertexConsumer> vertices
    ) {
        RenderType renderType = RenderType.create(name, RenderSetup.builder(pipeline).createRenderSetup());
        vertices.accept(bufferSource.getBuffer(renderType));
    }*/
    //?}
}
