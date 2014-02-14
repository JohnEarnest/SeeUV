import java.io.*;

/**
* A Resource is a file-backed data structure of some kind
* which should update itself when the source file is modified.
*
* @author John Earnest
**/

public abstract class Resource {

	public final File file;
	private long lastPoll;
	private long lastModified;

	public Resource(String filename) {
		this.file = new File(filename);
		lastPoll     = System.nanoTime();
		lastModified = file.lastModified();
		load();
	}

	public void poll() {
		// don't ping the filesystem more than once per second:
		long now = System.nanoTime();
		if (now - lastPoll < 1000000) { return; }
		lastPoll = now;

		// don't reload the file if it has not been modified:
		long mod = file.lastModified();
		if (lastModified == mod) { return; }
		lastModified = mod;

		free();
		load();
	}

	protected abstract void load();
	protected abstract void free();

}