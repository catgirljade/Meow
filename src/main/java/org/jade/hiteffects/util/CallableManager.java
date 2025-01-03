package org.jade.hiteffects.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CallableManager {
	private static final List<Callable<Boolean>> callables = new ArrayList<>();
	private static final List<Callable<Boolean>> callables_to_be_added = new ArrayList<>();

	public static void addCallable(Callable<Boolean> callable) {
		callables_to_be_added.add(callable);
	}

	public static void tick() {
		List<Callable<Boolean>> finished = new ArrayList<>();
		callables.forEach((callable) -> {
			try {
				if (callable.call() == Boolean.TRUE) {
					finished.add(callable);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		finished.forEach(callables::remove);
		callables.addAll(callables_to_be_added);
		callables_to_be_added.clear();
	}
}
