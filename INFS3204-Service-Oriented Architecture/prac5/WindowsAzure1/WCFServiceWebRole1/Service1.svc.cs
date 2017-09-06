using System;
using System.Collections;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WCFServiceWebRole1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IService1
    {
        public string GetData(int value)
        {
            return string.Format("You entered: {0}", value);
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }

        public string GetStringBack()
        {
            return "This will return a string: Window Azure!";
        }

        public string[][] AnagramsFinder(string[] words)
        {
            ArrayList wordList = new ArrayList(words);
            ArrayList sortedList = new ArrayList();
            ArrayList upperList = new ArrayList();
            ArrayList resultList = new ArrayList();
            ArrayList resultsList = new ArrayList();

            foreach(string word in wordList)
            {
                upperList.Add(word.ToUpper());
            }
            
            foreach (string word in upperList)
            {
                char[] sortedWord = word.ToArray();
                Array.Sort(sortedWord);
                sortedList.Add(new string(sortedWord));
            }

            foreach (string word in sortedList)
            {
                ArrayList groupList = new ArrayList();
                for (int i = 0; i < wordList.Count; i++)
                {
                    if (word == sortedList[i].ToString())
                    {
                        groupList.Add(wordList[i].ToString());
                    }
                }

                string group = string.Join(",", (string[])groupList.ToArray(typeof(string)));
                if (!resultList.Contains(group))
                {
                    resultList.Add(group);
                    resultsList.Add((string[])groupList.ToArray(typeof(string)));
                }
            }

            string[][] result = (string[][])resultsList.ToArray(typeof(string[]));
            return result;

        }

        public string ASCIIFilter(string words, int filter)
        {
            ArrayList resultList = new ArrayList();

            string[] Words = words.Split(',', ' ', '.', '?', '!', ':');
            foreach (string word in Words)
            {
                int total = 0;
                string eachCharacter = "";
                foreach (char character in word)
                {
                    int ASCIIofCharacter = (int)character;
                    eachCharacter = eachCharacter + ", " + character + ":" + ASCIIofCharacter;
                    total = total + ASCIIofCharacter;
                }
                if (total >= filter)
                {
                    resultList.Add("{" + word + ":" + total + eachCharacter + "}");
                }


            }

            string result = "{"+ string.Join(",", (string[])resultList.ToArray(typeof(string))) +"}";
            return result;
        }
    }
}
