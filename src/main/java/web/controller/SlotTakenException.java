package web.controller;

public class SlotTakenException extends Exception {

	private static final long serialVersionUID = 1L;

	public SlotTakenException(String message) {
		super(message);
	}
}
