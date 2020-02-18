package cn.xhzren.game.localrpg.entity.netting;

public class MovingAverage {

    private long[] samples;
    private long sum;
    private int count,index;

    /**
     * 初始化指定长度的数组
     * @param numSamples
     */
    public MovingAverage(int numSamples){
        samples = new long[numSamples];
    }

    public void add(long sample) {
        sum = sum - samples[index] + sample;
        samples[index++] = sample;
    }

    public long getAverage() {
        if(count == 0) {
            return count;
        }else {
            return (long)((float)sum/(float)count);
        }
    }
}
