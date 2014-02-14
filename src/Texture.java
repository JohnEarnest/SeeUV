import java.nio.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture extends Resource {

	public BufferedImage texture;
	public int id;

	public Texture(String filename) {
		super(filename);
	}

	protected void load() {
		try { texture = ImageIO.read(file); }
		catch(IOException e) { e.printStackTrace(); System.exit(0); }

		int w = texture.getWidth();
		int h = texture.getHeight();
		int[] data = texture.getData().getPixels(0, 0, w, h, new int[w*h*4]);

		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * w * h);
		for(int z = 0; z < data.length; z++) {
			buffer.put(z, (byte)(data[z]));
		}

		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glBindTexture(GL_TEXTURE_2D, 0);

		System.out.format("loaded texture '%s'%n", file);
	}

	protected void free() {
		glDeleteTextures(id);
	}
}