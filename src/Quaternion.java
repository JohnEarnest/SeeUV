/**
* An immutable Quaternion type.
* 
* @author John Earnest
**/

public class Quaternion {
	
	public final float w;
	public final float x;
	public final float y;
	public final float z;
	public static Quaternion IDENTITY = new Quaternion(1, 0, 0, 0);

	public Quaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion(float w, Vector3 v) {
		this(w, v.x, v.y, v.z);
	}

	public float length() {
		return (float)Math.sqrt(
			(w * w) +
			(x * x) +
			(y * y) +
			(z * z)
		);
	}

	public Quaternion normalize() {
		float len = length();
		return new Quaternion(
			(w / len),
			(x / len),
			(y / len),
			(z / len)
		);
	}

	public float dot(Quaternion other) {
		return
			(w * other.w) +
			(x * other.x) +
			(y * other.y) +
			(z * other.z)
		;
	}

	public Quaternion mul(Quaternion other) {
		return new Quaternion(
			(w * other.w) - (x * other.x) - (y * other.y) - (z * other.z),
			(w * other.x) + (x * other.w) + (y * other.z) - (z * other.y),
			(w * other.y) + (y * other.w) + (z * other.x) - (x * other.z),
			(w * other.z) + (z * other.w) + (x * other.y) - (y * other.x)
		);
	}

	public float angle() {
		return (float)(Math.acos(w) * 2);
	}

	public Vector3 axis() {
		float s = (float)Math.sqrt(1 - (w * w));

		// for small rotations, the direction of the axis
		// is not significant. Also avoid a division by zero:
		if (s < .0001) { s = 1; }

		return new Vector3((x / s), (y / s), (z / s));
	}

	public String toString() {
		return String.format("<<%f, %f, %f, %f>>", w, x, y, z);
	}
}