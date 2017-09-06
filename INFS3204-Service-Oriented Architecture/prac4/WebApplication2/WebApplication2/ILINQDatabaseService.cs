using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication2
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "ILINQDatabaseService" in both code and config file together.
    [ServiceContract]
    public interface ILINQDatabaseService
    {
        [OperationContract]
        Boolean PatientRegistration(string healthInsuranceNO, string fname, string lname, int pnumber, string address, string email);

        [OperationContract]
        Patient GetPatientInfo(string fname, string lname);

        [OperationContract]
        Boolean DoctorRegistration(string medicalRegistrationNO, string fname, string lname, string HealthProfession, int pnumber, string email);

        [OperationContract]
        Doctor GetDoctorInfo(string fname, string lname);

        [OperationContract]
        Boolean AppointmentBooking(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime, string cname);

        [OperationContract]
        Appointment GetAppointment(string pfname, string plname, string dfname, string dlname);

        [OperationContract]
        Boolean AppointmentReschedule(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime);
    }
}
