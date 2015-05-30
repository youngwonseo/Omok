
public class OmokController {
	private final int ROWS = 10;
	private final int COLS = 10;

	
	
	private final int[] CHECK_OMOK_X = {-1, 0, 1, 1};
	private final int[] CHECK_OMOK_Y = {-1,-1,-1, 0};
	
	private int [][]gameBoard = new int[ROWS][COLS];
	private final NetworkController nc;
	
	private boolean color;
	private boolean isMyTurn = false;
	
	int mySton = 0;
	
	
	public OmokController(){
		nc = new NetworkController(this);		
		
	}
	
	
	public void startGame(){
		
	}
	
	
	public boolean toClick(boolean isMyClick,int x,int y){
		if(isMyClick && x >= 0 && x < ROWS && y >= 0 && y < COLS && gameBoard[y][x] == 0){
			gameBoard[y][x] = mySton;
			nc.toSendMessage(new Message(MessageType.COORDINATE,x+","+y));
			if(isWin(x,y)){
				toWin();
				nc.toSendMessage(new Message(MessageType.WIN,null));
			}
		}else if(!isMyClick){
			gameBoard[y][x] = (mySton == 1) ? 2 : 1;
		}else{
			return false;
		}
		
		isMyTurn = !isMyTurn;
		return true;
	}
	
	public int getXYInfo(int x,int y){
		return gameBoard[y][x];
	}
	
	public boolean toChat(String chat){
		nc.toSendMessage(new Message(MessageType.CHAT,chat));
		return true;
	}
	
	
	public boolean checkOmok(){
		
		return false;
	}
	
	public boolean toConnactServer(String ip,OmokUI ou){
		mySton = 2;
		isMyTurn = false;
		nc.client(ip,3003,ou);
		
		return true;
	}
	
	
	public boolean isMyTuren(){
		return isMyTurn;
	}
	public void setMyTrun(boolean myTurn){
		isMyTurn = myTurn;
	}
	
	public void sendMessage(MessageType type,String data){
		nc.toSendMessage(new Message(MessageType.COORDINATE,data));
	}
	
	public void getChat(String chat){
		
		
	}
	
	public void getEnemy(int x,int y){
		toClick(false,x,y);
	}
	
	
	public boolean toWaitClient(OmokUI ou){
			
		mySton = 1;
		isMyTurn = true;
		
		return nc.server(3003,ou);
	}
	
	public void stopToWaitClient(){
		
		nc.stopWaitClient();
	}
	
	public boolean isWin(int x,int y){
		
		int numOfSton = 0;
		int cur_x = 0;
		int cur_y = 0;
		
		for(int i=0;i<4;i++){
			numOfSton = 0;
			for(int j= 5; j > -6; j--){
				
				cur_x = x + CHECK_OMOK_X[i] * j;
				cur_y = y + CHECK_OMOK_Y[i] * j;
						
				if(cur_x < 0 || cur_y < 0 || cur_x >= COLS || cur_y >= ROWS){
					continue;
				}
				
				if(gameBoard[cur_y][cur_x] == mySton)
					numOfSton++;
				else if(gameBoard[cur_y][cur_x] == (mySton == 1 ? 2 : 1))
					numOfSton = 0;
				
				if(numOfSton == 5){					
					return true;
				}		
			}
		}		
		return false;
	}
	
	public void toWin(){
		System.out.println("Win");
		isMyTurn = false;
	}
	public void toLose(){
		System.out.println("Lose");
		isMyTurn = false;
	}
}
