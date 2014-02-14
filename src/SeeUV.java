import java.io.*;
import java.nio.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

public class SeeUV {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("usage: seeuv <model> <texture>");
			System.exit(0);
		}
		System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		model   = new Model(args[0]);
		texture = new Texture(args[1]);
		Display.setTitle("SeeUV - " + model.file.getName());

		setup();
		long lastframe = (System.nanoTime() / 1000000);
		while(!Display.isCloseRequested()) {
			long thisframe = (System.nanoTime() / 1000000);
			tick((int)(thisframe - lastframe));
			draw();
			lastframe = thisframe;
			Display.update();
			texture.poll();
			model.poll();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	static float hrot = 0;
	static float vrot = 0;
	static float scale = 150;

	static Texture texture;
	static Model model;

	static float rx = 0;
	static float ry = 0;
	static int dx = 0;
	static int dy = 0;
	static boolean dragging;

	static void setup() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, 400, -400);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
	}

	static void tick(int delta) {
		if (Mouse.isButtonDown(0)) {
			if (dragging) {
				rx = (float)((dx - Mouse.getX()) * 0.5);
				ry = (float)((dy - Mouse.getY()) * 0.5);
			}
			else {
				dragging = true;
				dx = Mouse.getX();
				dy = Mouse.getY();
			}
		}
		else {
			if (dragging) {
				dragging = false;
				hrot = (hrot + rx) % 360;
				vrot = (vrot + ry) % 360;
				rx = 0;
				ry = 0;
			}
		}
		scale += Mouse.getDWheel();
		if (scale <   10) { scale =   10; }
		if (scale > 1000) { scale = 1000; }
	}

	static void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.id);
		glEnable(GL_TEXTURE_2D);

		glPushMatrix();
		glTranslatef(320, 240, 0);
		glScalef(scale, scale, scale);
		glRotatef(hrot + rx, 0, 1, 0);
		glRotatef(vrot + ry, 1, 0, 0);

		glBegin(GL_TRIANGLES);
		for(Model.Triangle t : model.triangles) {
			for(int i = 0; i < 3; i++) {
				Model.Texture c = t.textures[i];
				Model.Vertex  p = t.vertices[i];
				glTexCoord2f(c.u, c.v * -1);
				glVertex3f(p.data[0], p.data[1], p.data[2]);
			}
		}
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
}