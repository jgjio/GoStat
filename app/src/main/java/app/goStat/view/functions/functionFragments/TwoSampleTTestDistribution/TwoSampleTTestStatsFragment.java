package app.goStat.view.functions.functionFragments.TwoSampleTTestDistribution;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import app.goStat.R;
import app.goStat.util.android.ClipboardUtil;
import app.goStat.view.functions.functionFragments.TestStatisticsFragment;

public class TwoSampleTTestStatsFragment extends TestStatisticsFragment {
    View mRootView;

    EditText mAlphaOptionalEditText;
    EditText mListOneSampleMean;
    EditText mListOneStandardDeviation;
    EditText mListOneSampleSize;
    EditText mListTwoSampleMean;
    EditText mListTwoStandardDeviation;
    EditText mListTwoSampleSize;
    RadioGroup mPooledRadioGroup;
    TextView mOutputView;
    Button mCalculate;
    private boolean isPooled;

    private String mAnswer = "";
    private String mOutput = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflateFragment(R.layout.fragment_two_sample_t_test_stats,inflater,container);
        mAlphaOptionalEditText = mRootView.findViewById(R.id.alpha_edit_text);

        mListOneSampleMean = mRootView.findViewById(R.id.list_one_sample_mean_edit_text);
        mListOneStandardDeviation = mRootView.findViewById(R.id.list_one_standard_deviation_edit_text);
        mListOneSampleSize = mRootView.findViewById(R.id.list_one_sample_size_edit_text);
        mListTwoSampleMean = mRootView.findViewById(R.id.list_two_sample_mean_edit_text);
        mListTwoStandardDeviation = mRootView.findViewById(R.id.list_two_standard_deviation_edit_text);
        mListTwoSampleSize = mRootView.findViewById(R.id.list_two_sample_size_edit_text);
        mPooledRadioGroup = mRootView.findViewById(R.id.pooled_radio_group);
        mCalculate = mRootView.findViewById(R.id.calculate_button);
        mOutputView = mRootView.findViewById(R.id.output_text_view);

        Button copyAnswer =  mRootView.findViewById(R.id.copy_answer_button);

        copyAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAnswerToClipboard();
            }
        });

        Button copyAllText =  mRootView.findViewById(R.id.copy_all_text_button);

        copyAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAllTextToClipboard();
            }
        });

        isPooled = false; //default
        createPooledRadioGroupListener();


        return mRootView;
    }

    protected void createPooledRadioGroupListener () {
        RadioGroup variances = mRootView.findViewById(R.id.pooled_radio_group);
        variances.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch(checkedID) {
                    case R.id.no_pooled_radio_button:
                        isPooled = false;
                        break;
                    case R.id.yes_pooled_radio_button:
                        isPooled = true;
                        break;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mRootView.findViewById(R.id.calculate_button).findViewById(R.id.calculate_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCalculatedClicked();
                    }
                });
    }

    private void copyAnswerToClipboard(){
        ClipboardUtil clip = new ClipboardUtil();
        clip.copyToClipboard(mAnswer, getActivity());
        clip.showCopyToClipboardMessage(getActivity());
    }

    private void copyAllTextToClipboard(){
        ClipboardUtil clip = new ClipboardUtil();
        clip.copyToClipboard(mOutput,getActivity());
        clip.showCopyToClipboardMessage(getActivity());
    }

    public static TwoSampleTTestStatsFragment newInstance() {
        return new TwoSampleTTestStatsFragment();
    }

    @Override
    protected boolean isAnInputEmpty() {
        return "".equals(mListOneSampleMean.getText().toString()) ||
                "".equals(mListOneStandardDeviation.getText().toString()) ||
                "".equals(mListOneSampleSize.getText().toString()) ||
                "".equals(mListTwoSampleMean.getText().toString()) ||
                "".equals(mListTwoStandardDeviation.getText().toString()) ||
                "".equals(mListTwoSampleSize.getText().toString());
    }

    @Override
    protected boolean doesEditTextContainError() {
        return mListOneSampleMean.getError() != null ||
                mListOneStandardDeviation.getError() != null ||
                mListOneSampleSize.getError() != null ||
                mListTwoSampleMean.getError() != null ||
                mListTwoStandardDeviation.getError() != null ||
                mListTwoSampleSize.getError() != null;
    }

    @Override
    protected void calculateEqualityVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledTwoTailedDistributionTest(getData());
        } else{
            distributionTest = new NotPooledTwoTailedDistributionTest(getData());
        }

        mOutputView.setText(distributionTest.toString());
        mOutput = distributionTest.toString();
        mAnswer = "t = " + distributionTest.getT() + " \n"
        + "p = " + distributionTest.getP();
    }

    @Override
    protected void calculateMoreThanVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledMoreThanDistributionTest(getData());
        } else{
            distributionTest = new NotPooledMoreThanDistributionTest(getData());
        }
        mOutputView.setText(distributionTest.toString());
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    protected void calculateLessThanVariance() {
        TDistributionTest distributionTest;

        if (isPooled) {
            distributionTest = new PooledLessThanDistributionTest(getData());
        } else{
            distributionTest = new NotPooledLessThanDistributionTest(getData());
        }
        mOutputView.setText(distributionTest.toString());
        mAnswer = "t = " + distributionTest.getT() + " \n"
                + "p = " + distributionTest.getP();
    }

    @Override
    protected EditText getAlphaEditText() {
        return mAlphaOptionalEditText;
    }

    private TTestData getData() {
        return new TTestData(Double.parseDouble(mListOneSampleSize.getText().toString()),
                Double.parseDouble(mListOneSampleMean.getText().toString()),
                Double.parseDouble(mListOneStandardDeviation.getText().toString()),
                Double.parseDouble(mListTwoSampleSize.getText().toString()),
                Double.parseDouble(mListTwoSampleMean.getText().toString()),
                Double.parseDouble(mListTwoStandardDeviation.getText().toString()));
    }
}
