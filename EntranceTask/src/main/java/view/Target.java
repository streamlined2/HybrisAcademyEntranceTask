package view;

@FunctionalInterface
public interface Target {
	
	Object perform(Object arg) throws Exception;

}
