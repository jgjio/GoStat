package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

import java.util.List;
import app.goStat.model.DataPoint;
import app.goStat.util.StatisticsTests.TTestUtil;

class PooledMoreThanDistributionTest extends PooledVarianceDistributionTest {
    public PooledMoreThanDistributionTest(TTestData data){
        super(data);
    }

    public PooledMoreThanDistributionTest(List<DataPoint> list1, List<DataPoint> list2) {
        super(list1, list2);
    }

    @Override
    public double getP() {
        return new TTestUtil().getPOfPooledTwoSampleTTestMoreThan(data.xBar1,data.xBar2,data.sX1,data.sX2,data.n1,data.n2);
    }
    @Override
    public String toString() {
        return super.toString() +
                "variance type: more than";
    }
}

