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
        /** ����ѵ������*/
        File inputFile = new File("D:\\dataset\\cpu.with.vendor.arff");
        ArffLoader atf = new ArffLoader();
        atf.setFile(inputFile);
        Instances instancesTrain = atf.getDataSet();
        
        /** �����������*/
        inputFile = new File("D:\\dataset\\cpu.with.vendor.arff");
        atf.setFile(inputFile);
        Instances instancesTest = atf.getDataSet();
        /** ���÷������������к� */
        instancesTrain.setClassIndex(0);
        instancesTest.setClassIndex(0);
        double num = instancesTest.numInstances();
        double right = 0.0f; //ʶ�����õ�
        System.out.println(num);
        /** ģ��ѵ��*/
        m_classifier.buildClassifier(instancesTrain);

        for(int i = 0;i <num;i++){
            /** ���Ԥ��ֵ�ʹ�ֵ���*/
            if(m_classifier.classifyInstance(instancesTest.instance(i))
                    ==instancesTest.instance(i).classValue()){
                right++;
            }
        }
        System.out.println("RandomForest classification precision:"+ (right)/num);
        //����ģ��
        SerializationHelper.write("LibSVM.model", m_classifier);
        
        //��ȡģ��
        Classifier classifier8 = (Classifier) weka.core.SerializationHelper.read("LibSVM.model");
        double right2 = 0.0f;
        for(int i = 0;i <num;i++){
            /** ���Ԥ��ֵ�ʹ�ֵ���*/
            if(classifier8.classifyInstance(instancesTest.instance(i))
                    ==instancesTest.instance(i).classValue()){
                right2++;
            }
            System.out.println(instancesTest.instance(i).classValue());
        }
        System.out.println("RandomForest classification precision(from model):"+ (right2)/num);
    }
}
