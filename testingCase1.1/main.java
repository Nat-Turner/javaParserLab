public class Main
{
    public static void main(String [] args)
    {
        Company c = new Company();
        Worker w = new Worker();
        Job j = new Job();
        c.addJob(j);
        c.addWorker(w);
        w.assign(j);
        w.setEmotion("happy");
        j.setDefaultiy("hard");

    }
}