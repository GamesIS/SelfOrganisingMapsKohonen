package sample;

/*public class KohonensNetwork
{
    private const int EpochCount = 100;

    private static final Random Random = new Random();

    private double _lambda;
    private double _learningRadius;
    private double _learningFactor;
    private double[][] _neirons;
    private double[][][] _weights;

    public KohonensNetwork(int inputsCount, int neironsCount)
    {
        InitializeNeironsAndWeights(inputsCount, neironsCount);

        _learningRadius = neironsCount / 2;
        _learningFactor = 1;

        _lambda = EpochCount / Math.Log(_learningRadius);
    }

    public void DoLearn(double[][] inputSet)
    {
        for (int i = 0; i < EpochCount; i++)
        {
            var index = i % inputSet.Length;

            DoLearnIteration(inputSet[index], i);
        }
    }

    public double[][] GetWeights(int weightIndex)
    {
        var result = new List<double[]>();

        for (int i = 0; i < _weights.GetLength(dimension: 0); i++)
        {
            var line = new List<double>();

            for (int j = 0; j < _weights.GetLength(dimension: 1); j++)
            {
                line.Add(_weights[i, j, weightIndex]);
            }

            result.Add(line.ToArray());
        }

        return result.ToArray();
    }

    private void InitializeNeironsAndWeights(int inputsCount, int neironsCount)
    {
        _neirons = new double[neironsCount, neironsCount];
        _weights = new double[neironsCount, neironsCount, inputsCount];

        for (int i = 0; i < _weights.GetLength(dimension: 0); i++)
        {
            for (int j = 0; j < _weights.GetLength(dimension: 1); j++)
            {
                for (int k = 0; k < _weights.GetLength(dimension: 2); k++)
                {
                    _weights[i, j, k] = Random.NextDouble();
                }
            }
        }
    }

    private void DoLearnIteration(double[] inputs, int iterationCount)
    {
        var lengthX = _neirons.GetLength(dimension: 0);
        var lengthY = _neirons.GetLength(dimension: 1);
        var evklidRanges = new double[lengthX, lengthY];

        for (int i = 0; i < evklidRanges.GetLength(dimension: 0); i++)
        {
            for (int j = 0; j < evklidRanges.GetLength(dimension: 1); j++)
            {
                evklidRanges[i, j] = GetEvklidRange(i, j, inputs);
            }
        }

        (int x, int y) = GetIndexOfMin(evklidRanges);

        CorrectWeightsAround(x, y, iterationCount, inputs);
    }

    private double GetEvklidRange(int x, int y, double[] inputSet)
    {
        var result = 0.0;

        for (int i = 0; i < inputSet.Length; i++)
        {
            result += Math.Pow(_weights[x, y, i] - inputSet[i], 2);
        }

        return Math.Sqrt(result);
    }

    private void CorrectWeightsAround(int x, int y, int iterationCount, double[] inputSet)
    {
        var learningFactor = GetDecreasedLearningFactor(iterationCount);
        var learningRadius = iterationCount == 0 ? _learningRadius
                : GetDecreasedLearningRadius(iterationCount);

        var centerTetta = GetTetta(x, y, x, y, learningRadius);
        CorrectWeights(x, y, centerTetta, learningFactor, inputSet);

        for (int i = 0; i < _weights.GetLength(dimension: 0); i++)
        {
            for (int j = 0; j < _weights.GetLength(dimension: 1); j++)
            {
                if (i != x || j != y)
                {
                    var tetta = GetTetta(x, y, i, j, learningRadius);

                    CorrectWeights(i, j, tetta, learningFactor, inputSet);
                }
            }
        }
    }

    private void CorrectWeights(int x, int y, double tetta, double learningFactor, double[] inputSet)
    {
        if (x >= 0 && x < _weights.GetLength(dimension: 0) &&
        y >= 0 && y < _weights.GetLength(dimension: 1))
        {
            for (int i = 0; i < inputSet.Length; i++)
            {
                _weights[x, y, i] += tetta * learningFactor * (inputSet[i] - _weights[x, y, i]);
            }
        }
    }

    private (int x, int y) GetIndexOfMin(double[,] ranges)
{
    var currentValue = double.MaxValue;
    var indexX = -1;
    var indexY = -1;

    for (int i = 0; i < ranges.GetLength(dimension: 0); i++)
    {
        for (int j = 0; j < ranges.GetLength(dimension: 1); j++)
        {
            if (ranges[i, j] < currentValue)
            {
                currentValue = ranges[i, j];
                indexX = i;
                indexY = j;
            }
        }
    }

    return (indexX, indexY);
}

    private double GetDecreasedLearningRadius(int iterationCount)
    {
        return _learningRadius * Math.Exp(-iterationCount / _lambda);
    }

    private double GetDecreasedLearningFactor(int iterationCount)
    {
        return _learningFactor * Math.Exp(-iterationCount / (double)EpochCount);
    }

    private double GetTetta(int bmuX, int bmuY, int x, int y, double learningRadius)
    {
        var delta = Math.Sqrt((bmuX - x) * (bmuX - x) + (bmuY - y) * (bmuY - y));

        return Math.Exp(-(delta * delta) / (2 * learningRadius * learningRadius));
    }
}*/