package view;

@FunctionalInterface
public interface Target {
	
	Object accept(Object arg) throws Exception;

}
