package helpers;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    private String[] characters = {"Super","JumpinGirl","Man","Woman"};
    private int index =0;



    private GameManager()
    {

    }

    public void incrementIndex()
    {
        index++;
        if(index==characters.length)
        {
            index=0;
        }
    }

    public String getCharacter()
    {
        return characters[index];
    }


    public static GameManager getInstance() {
        return ourInstance;
    }
}
