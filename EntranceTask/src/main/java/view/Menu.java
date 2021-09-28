package view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Menu {
	
	private record Action (String name, Target measure) {
		public Action {
			Objects.requireNonNull(name, "name of the action shouldn't be null");
			Objects.requireNonNull(measure, "please provide non-null measure for given action");
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	private final Map<String,Action> menu = new LinkedHashMap<>();
	
	public Menu add(String option, String name, Target action) {
		menu.put(option, new Action(name, action));
		return this;
	}
	
	public Optional<Object> act(String option, Object arg) throws Exception {
		Action action = menu.get(option);
		if(action != null) {
			return Optional.of(action.measure().perform(arg));
		}
		return Optional.empty();
	}
	
	private static String toString(Entry<String, Action> entry) {
		return entry.getKey()+": "+entry.getValue().toString();
	}
	
	@Override
	public String toString() {
		return menu.entrySet().stream().map(Menu::toString).collect(Collectors.joining("\n"));
	}

}
