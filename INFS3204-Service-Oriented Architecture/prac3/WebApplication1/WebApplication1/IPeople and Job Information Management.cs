using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IPeople_and_Job_Information_Management" in both code and config file together.
    [ServiceContract]
    public interface IPeople_and_Job_Information_Management
    {
        [OperationContract]
        void DoWork();

        [OperationContract]
        Boolean SaveInfo(Person person);

        [OperationContract]
        Job GetJobInfo(string fname, string lname);

        [OperationContract]
        List<Person> GetColleagues(string fname, string lname);
    }

    [DataContract]
    public class Person
    {
        [DataMember]
        public string firstName { get; set; }
        [DataMember]
        public string lastName { get; set; }
        [DataMember]
        public DateTime dateOfBirth { get; set; }
        [DataMember]
        public string email { get; set; }
        [DataMember]
        public string streetAddress { get; set; }
        [DataMember]
        public string suburb { get; set; }
        [DataMember]
        public string state { get; set; }
        [DataMember]
        public int postcode { get; set; }
        [DataMember]
        public Job job { get; set; }
    }

    [DataContract]
    public class Job
    {
        [DataMember]
        public int positionNumber;
        [DataMember]
        public string positionTitle;
        [DataMember]
        public string positionDescription;
        [DataMember]
        public string companyName;
    }
}
