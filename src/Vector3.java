/**
* An immutable Vector3 type.
* 
* @author John Earnest
**/

public class Vector3 {

	public final float x;
	public final float y;
	public final float z;

	public static final Vector3 ZERO = new Vector3(0, 0, 0);

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float length() {
		return (float)Math.sqrt(dot(this));
	}

	public Vector3 normalize() {
		float len = length();
		return new Vector3(
			(x / len),
			(y / len),
			(z / len)
		);
	}

	public Vector3 cross(Vector3 other) {
		return new Vector3(
			(y * other.z) - (z * other.y),
			(z * other.x) - (x * other.z),
			(x * other.y) - (y * other.x)
		);
	}

	public float dot(Vector3 other) {
		return
			(x * other.x) +
			(y * other.y) +
			(z * other.z)
		;
	}

	public Vector3 mul(float n) {
		return new Vector3(
			(x * n),
			(y * n),
			(z * n)
		);
	}

	public Vector3 add(Vector3 other) {
		return new Vector3(
			(x + other.x),
			(y + other.y),
			(z + other.z)
		);
	}

	public Vector3 sub(Vector3 other) {
		return new Vector3(
			(x - other.x),
			(y - other.y),
			(z - other.z)
		);
	}

	public String toString() {
		return String.format("<%f, %f, %f>", x, y, z);
	}
}