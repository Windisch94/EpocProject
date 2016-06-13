package rr.mc.fhhgb.at.epocgame;

public interface EngineInterface {
	//train
	void trainStarted();

	void trainSucceed();

	void trainFailed();

	void trainCompleted();

	void trainRejected();

	void trainReset();

	void trainErased();


	
	//action
	void currentAction(int typeAction, float power);
	
	
}
