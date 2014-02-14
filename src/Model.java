import java.nio.*;
import java.io.*;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Model extends Resource {

	public List<Vertex>   vertices;
	public List<Texture>  textures;
	public List<Triangle> triangles;

	public Model(String filename) {
		super(filename);
	}

	protected void load() {
		Scanner in = null;
		try { in = new Scanner(file); }
		catch(FileNotFoundException e) { e.printStackTrace(); System.exit(0); }

		if (vertices == null) {
			vertices = new ArrayList<Vertex>();
			textures = new ArrayList<Texture>();
			triangles = new ArrayList<Triangle>();
		}

		while(in.hasNextLine()) {
			final Scanner line = new Scanner(in.nextLine());
			final String command = line.next();

			if ("v".equals(command)) {
				vertices.add(new Vertex(
					line.nextFloat(),
					line.nextFloat(),
					line.nextFloat()
				));
			}
			if ("vt".equals(command)) {
				textures.add(new Texture(
					line.nextFloat(),
					line.nextFloat()
				));
			}
			if ("f".equals(command)) {
				// triangulate faces as necessary:
				List<int[]> corners = new ArrayList<int[]>();
				while(line.hasNext()) {
					String[] pair = line.next().split("/");
					corners.add(new int[] {
						Integer.parseInt(pair[0]) - 1,
						Integer.parseInt(pair[1]) - 1
					});
				}
				for(int z = 1; z < corners.size()-1; z++) {
					triangles.add(new Triangle(
						new Vertex [] {
							vertices.get(corners.get(0)  [0]),
							vertices.get(corners.get(z)  [0]),
							vertices.get(corners.get(z+1)[0])
						},
						new Texture [] {
							textures.get(corners.get(0)  [1]),
							textures.get(corners.get(z)  [1]),
							textures.get(corners.get(z+1)[1])
						}
					));
				}
			}
		}
		center();

		System.out.format("loaded model '%s'%n", file);
	}

	public void free() {
		vertices.clear();
		textures.clear();
		triangles.clear();
	}

	private void center() {
		float[] min = new float[] {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
		float[] max = new float[] {Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE};
		for(Vertex v : vertices) {
			for(int a = 0; a < 3; a++) {
				min[a] = Math.min(min[a], v.data[a]);
				max[a] = Math.max(max[a], v.data[a]);
			}
		}
		float[] avg = new float[3];
		for(int a = 0; a < 3; a++) {
			avg[a] = min[a] + (max[a] - min[a])/2;
		}
		for(Vertex v : vertices) {
			for(int a = 0; a < 3; a++) {
				v.data[a] -= avg[a];
			}
		}
	}

	static class Vertex {
		float[] data;

		Vertex(float... data) {
			this.data = data;
		}
	}

	static class Texture {
		float u;
		float v;

		Texture(float u, float v) {
			this.u = u;
			this.v = v;
		}
	}

	static class Triangle {
		final Vertex [] vertices;
		final Texture[] textures;

		Triangle(Vertex[] vertices, Texture[] textures) {
			this.vertices = vertices;
			this.textures = textures;
		}
	}
}