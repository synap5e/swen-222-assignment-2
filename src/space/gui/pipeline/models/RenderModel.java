package space.gui.pipeline.models;

/** A model that can be rendered
 * 
 * All models to draw must implement RenderModel.
 * RenderModel may hold a heirachy of RenderModels to create a scene graph.
 * 
 * @author Simon Pinfold (300280028)
 *
 */
public interface RenderModel {
	
	/**
	 * Render the model at using the current transform stack.
	 * Undefined behavior if the current opengl matrix mode is not modelview.
	 */
	public void render();

}
