package space.gui.pipeline;

public interface ViewableRoom {

	public enum LightMode{ BASIC_LIGHT };
	
	/** Gets the lighting mode for a room. Currently this can only return BASIC_LIGHT
	 * 
	 * @return LightMode.BASIC_LIGHT
	 */
	public LightMode getLightMode();

}
