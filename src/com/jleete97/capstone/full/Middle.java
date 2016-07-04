package com.jleete97.capstone.full;

/**
 * Middle component of a chain of {@link LineReader}s and {@link LineWriter}s.
 * <p>
 * Processes a line of text from a {@link LineWriter}, passes along something
 * (or nothing) to the next {@link LineReader}.
 */
public abstract class Middle extends BaseLineWriter
		implements LineReader, LineWriter {
	
	/**
	 * Do something with the input {@code line}. Can return anything: if an
	 * {@link Iterable}, it gets iterated over, otherwise just converted to
	 * {@code String} form and passed on.
	 * 
	 * @param line The {@code String} line to act upon.
	 * @return The result of acting on that line.
	 */
	protected abstract Object process(String line);
	
	private final void processInternally(String line) {
		if (line != null) {
			Object o = process(line);
			sendObject(o);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private final void sendObject(Object o) {
		if (o != null) {
			if (o instanceof Object[]) {
				for (Object elt : (Object []) o) {
					sendObject(elt);
				}
			} else if (o instanceof Iterable) {
				for (Object elt : (Iterable) o) {
					sendObject(elt);
				}
			} else {
				send(o.toString());
			}
		}
	}
	
	@Override
	public void send(String line) {
		this.reader.receive(line);
	}

	@Override
	public void receive(String line) {
		processInternally(line);
	}

	@Override
	public void done() { }
}
