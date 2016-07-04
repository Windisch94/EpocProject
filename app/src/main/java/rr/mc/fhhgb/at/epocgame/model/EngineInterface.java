package rr.mc.fhhgb.at.epocgame.model;

/**
 * Interface for the training
 * @author Windischhofer, Rohner
 */
public interface EngineInterface {
	//train
	void trainStarted();

	void trainSucceed();

	void trainFailed();

	void trainCompleted();

	void trainRejected();

	void trainReset();

	void trainErased();

	public void userAdd(int userId);

	public void userRemoved();


	
	//action
	void currentAction(int typeAction, float power);
	
	
}
