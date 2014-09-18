package com.rakov.saldo.model;

import java.util.Arrays;
import java.util.List;

public class SemanticCompoundSupport {
	@Override
	public String toString() {
		return "SemanticCompoundSupport [flagIsComp=" + flagIsComp + ", parts="
				+ Arrays.toString(parts) + "]";
	}

	private boolean flagIsComp;
	private String[] parts;

	public boolean isFlagIsComp() {
		return flagIsComp;
	}

	public void setFlagIsComp(boolean flagIsComp) {
		this.flagIsComp = flagIsComp;
	}

	public String[] getParts() {
		return parts;
	}

	public void setParts(String[] parts) {
		this.parts = parts;
	}

}
