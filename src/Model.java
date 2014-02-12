import java.nio.*;
import java.io.*;
import java.util.*;

public class Model {

	public static FloatBuffer load(String filename) throws FileNotFoundException {
		Scanner in = new Scanner(new File(filename));
		List<Vertex>  vertices = new ArrayList<Vertex>();
		List<Texture> textures = new ArrayList<Texture>();
		List<Face>    faces    = new ArrayList<Face>();
	
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
				Face f = new Face();
				faces.add(f);
				for(int z = 0; z < 4; z++) {
					String[] pair = line.next().split("/");
					f.vertices[z] = Integer.parseInt(pair[0]) - 1;
					f.textures[z] = Integer.parseInt(pair[1]) - 1;
				}
			}
		}

		FloatBuffer ret = FloatBuffer.allocate(faces.size() * 4 * 5);
		for(int z = 0; z < faces.size(); z++) {
			Face f = faces.get(z);
			for(int i = 0; i < 4; i++) {
				Vertex  v = vertices.get(f.vertices[i]);
				Texture t = textures.get(f.textures[i]);
				int base = (z * 4 * 5) + (i * 5);
				ret.put(base,   v.x);
				ret.put(base+1, v.y);
				ret.put(base+2, v.z);
				ret.put(base+3, t.u);
				ret.put(base+4, t.v);
			}
		}
		return ret;
	}

	private static class Vertex {
		final float x;
		final float y;
		final float z;

		Vertex(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static class Texture {
		final float u;
		final float v;

		Texture(float u, float v) {
			this.u = u;
			this.v = v;
		}
	}

	private static class Face {
		final int[] vertices = new int[4];
		final int[] textures = new int[4];
	}
}