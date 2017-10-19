public class Worker {



    public void setEmotion(String happy) {

    }

    public void assign(Job j) {
        Job tempJob = j;
        setEmotion("happy");
        tempJob.setDefaultiy("easy");
    }
}
