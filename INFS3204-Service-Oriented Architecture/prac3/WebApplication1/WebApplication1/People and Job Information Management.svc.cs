using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "People_and_Job_Information_Management" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select People and Job Information Management.svc or People and Job Information Management.svc.cs at the Solution Explorer and start debugging.
    public class People_and_Job_Information_Management : IPeople_and_Job_Information_Management
    {
        public void DoWork()
        {
        }

        public Boolean SaveInfo(Person person)
        {

            string path = System.AppDomain.CurrentDomain.BaseDirectory.ToString();
            string birth = person.dateOfBirth.ToString("yyyy-MM-dd");

            try
            {
                System.IO.StreamWriter sw = new System.IO.StreamWriter(path + "Person.txt", true);

                System.IO.StreamWriter sw1 = new System.IO.StreamWriter(path + "Job.txt", true);

                sw.WriteLine(person.firstName + " " + person.lastName + " " + birth + " " + person.email + " " + person.streetAddress + " " + person.suburb + " " + person.state + " " + person.postcode + " " + person.job.positionNumber);
                sw1.WriteLine(person.job.positionNumber + " " + person.job.positionTitle + " " + person.job.positionDescription + " " + person.job.companyName);
                
                sw.Close();
                sw1.Close();

                return true;
            }
            catch
            {
                return false;
            }
        }

        public Job GetJobInfo(string fname, string lname)
        {
            Job job = new Job();
            
            string path = System.AppDomain.CurrentDomain.BaseDirectory.ToString();

            System.IO.FileStream fs = new System.IO.FileStream(path + "Person.txt", System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.StreamReader sr = new System.IO.StreamReader(fs, System.Text.Encoding.Default);

            System.IO.FileStream fs1 = new System.IO.FileStream(path + "Job.txt", System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.StreamReader sr1 = new System.IO.StreamReader(fs1, System.Text.Encoding.Default);

            string line;
            string pnumber="";

            while ((line = sr.ReadLine()) != null)
            {
                string[] infor = line.Split(' ');
                if (infor[0] == fname && infor[1] == lname)
                {
                    pnumber = infor[8];
                    break;
                }
            }

            if (pnumber != "")
            {
                string jobLine;

                while ((jobLine = sr1.ReadLine()) != null)
                {
                    string[] jobInfor = jobLine.Split(' ');
                    if (jobInfor[0] == pnumber)
                    {
                        job.positionNumber = Convert.ToInt32(jobInfor[0]);
                        job.positionTitle = jobInfor[1];
                        job.positionDescription = jobInfor[2];
                        job.companyName = jobInfor[3];
                        sr.Close();
                        sr1.Close();
                        return job;
                    }
                }
            }
            sr.Close();
            sr1.Close();      
            return null;
        }

        public List<Person> GetColleagues(string fname, string lname)
        {
            if (GetJobInfo(fname, lname) != null)
            {
                string cname = GetJobInfo(fname, lname).companyName;

                string path = System.AppDomain.CurrentDomain.BaseDirectory.ToString();

                System.IO.FileStream fs = new System.IO.FileStream(path + "Person.txt", System.IO.FileMode.Open, System.IO.FileAccess.Read);
                System.IO.StreamReader sr = new System.IO.StreamReader(fs, System.Text.Encoding.Default);

                System.IO.FileStream fs1 = new System.IO.FileStream(path + "Job.txt", System.IO.FileMode.Open, System.IO.FileAccess.Read);
                System.IO.StreamReader sr1 = new System.IO.StreamReader(fs1, System.Text.Encoding.Default);

                string jobLine;
                List<string> jobList = new List<string>();
                while ((jobLine = sr1.ReadLine()) != null)
                {
                    string[] jobInfor = jobLine.Split(' ');
                    if (jobInfor[3] == cname)
                    {
                        jobList.Add(jobLine);
                    }
                }

                if (jobList.Count > 1)
                {
                    List<string> personList = new List<string>();
                    string line;

                    foreach (string s in jobList)
                    {
                        string[] jobInfor = s.Split(' ');
                        while ((line = sr.ReadLine()) != null)
                        {
                            string[] infor = line.Split(' ');
                            if (infor[8] == jobInfor[0])
                            {
                                personList.Add(infor[0] + " " + infor[1] + " " + infor[2] + " " + infor[3] + " " + infor[4] + " " + infor[5] + " " + infor[6] + " " + infor[7]);
                                break;
                            }
                        }
                    }

                    List<string> resultList = new List<string>();
                    for (int i = 0; i < personList.Count; i++)
                    {
                        resultList.Add(personList[i] + " " + jobList[i]);
                    }

                    List<Person> listPerson = new List<Person>();

                    foreach (string result in resultList)
                    {
                        Person person = new Person();
                        Job job = new Job();
                        string[] resultInfor = result.Split(' ');
                        if (resultInfor[0] != fname || resultInfor[1] != lname)
                        {
                            person.firstName = resultInfor[0];
                            person.lastName = resultInfor[1];
                            person.dateOfBirth = Convert.ToDateTime(resultInfor[2]);
                            person.email = resultInfor[3];
                            person.streetAddress = resultInfor[4];
                            person.suburb = resultInfor[5];
                            person.state = resultInfor[6];
                            person.postcode = Convert.ToInt32(resultInfor[7]);

                            job.positionNumber = Convert.ToInt32(resultInfor[8]);
                            job.positionTitle = resultInfor[9];
                            job.positionDescription = resultInfor[10];
                            job.companyName = resultInfor[11];
                            person.job = job;

                            listPerson.Add(person);
                        }
                    }
                    sr.Close();
                    sr1.Close();
                    return listPerson;
                }

                sr.Close();
                sr1.Close();
                return null;
            }
            else return null;
        }
    }
}
