import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import java.io.File;

/**
 * Created by cky on 2016/8/30 14:20.
 */
public class DEMO {

    public static void main(String[] args) throws  Exception {

        Classifier m_classifier = new RandomForest();
        /** 读入训练数据*/
        File inputFile = new File("D:\\dataset\\cpu.with.vendor.arff");
        ArffLoader atf = new ArffLoader();
        atf.setFile(inputFile);
        Instances instancesTrain = atf.getDataSet();
        
        /** 读入测试数据*/
        inputFile = new File("D:\\dataset\\cpu.with.vendor.arff");
        atf.setFile(inputFile);
        Instances instancesTest = atf.getDataSet();
        /** 设置分类属性所在行号 */
        instancesTrain.setClassIndex(0);
        instancesTest.setClassIndex(0);
        double num = instancesTest.numInstances();
        double right = 0.0f; //识别率用的
        System.out.println(num);
        /** 模型训练*/
        m_classifier.buildClassifier(instancesTrain);

        for(int i = 0;i <num;i++){
            /** 如果预测值和答案值相等*/
            if(m_classifier.classifyInstance(instancesTest.instance(i))
                    ==instancesTest.instance(i).classValue()){
                right++;
            }
        }
        System.out.println("RandomForest classification precision:"+ (right)/num);
        //保存模型
        SerializationHelper.write("LibSVM.model", m_classifier);
        
        //读取模型
        Classifier classifier8 = (Classifier) weka.core.SerializationHelper.read("LibSVM.model");
        double right2 = 0.0f;
        for(int i = 0;i <num;i++){
            /** 如果预测值和答案值相等*/
            if(classifier8.classifyInstance(instancesTest.instance(i))
                    ==instancesTest.instance(i).classValue()){
                right2++;
            }
            System.out.println(instancesTest.instance(i).classValue());
        }
        System.out.println("RandomForest classification precision(from model):"+ (right2)/num);
    }
}
