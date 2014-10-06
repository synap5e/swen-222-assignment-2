package space.gui.pipeline;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class TextureLoader {

	// rgba is 4 bytes
	private static final int BYTES_PER_PIXEL = 4;
	
	public static int loadTexture(File path) throws IOException{
		BufferedImage image = ImageIO.read(path);
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight() * BYTES_PER_PIXEL);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); 	// R
				buffer.put((byte) ((pixel >> 8) & 0xFF)); 	// G
				buffer.put((byte) (pixel & 0xFF)); 			// B
				buffer.put((byte) ((pixel >> 24) & 0xFF)); 	// A
			}
		}

		buffer.flip();

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		
		/*glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_LINEAR);*/
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(	GL_TEXTURE_2D, 
						0,
						GL_RGBA8, 
						image.getWidth(),
						image.getHeight(),
						0,
						GL_RGBA,
						GL_UNSIGNED_BYTE,
						buffer
					);

		return textureID;
	}

}