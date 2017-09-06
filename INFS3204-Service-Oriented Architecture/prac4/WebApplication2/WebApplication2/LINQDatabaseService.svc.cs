using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WebApplication2
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "LINQDatabaseService" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select LINQDatabaseService.svc or LINQDatabaseService.svc.cs at the Solution Explorer and start debugging.
    public class LINQDatabaseService : ILINQDatabaseService
    {
        DataClasses1DataContext db = new DataClasses1DataContext();
        
        public Boolean PatientRegistration(string healthInsuranceNO, string fname, string lname, int pnumber, string address, string email)
        {
            try
            {
                var newPatient = new Patient();
                newPatient.Health_Insurance_NO_ = healthInsuranceNO;
                newPatient.First_name = fname;
                newPatient.Last_name = lname;
                newPatient.Phone_number = pnumber;
                newPatient.Address = address;
                newPatient.Email = email;
                db.Patients.InsertOnSubmit(newPatient);
                db.SubmitChanges();
                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }

        }

        public Patient GetPatientInfo(string fname, string lname)
        {
            try
            {
                Patient result = db.Patients.Single<Patient>(patient => patient.First_name == fname && patient.Last_name == lname);
                return result;
            }
            catch
            {
                throw new Exception("Not found");
            }
        }

        public Boolean DoctorRegistration(string medicalRegistrationNO, string fname, string lname, string HealthProfession, int pnumber, string email)
        {
            try
            {
                var newDoctor = new Doctor();
                newDoctor.Medical_Registration_NO_ = medicalRegistrationNO;
                newDoctor.First_name = fname;
                newDoctor.Last_name = lname;
                newDoctor.Health_Profession = HealthProfession;
                newDoctor.Phone_number = pnumber;
                newDoctor.Email = email;
                db.Doctors.InsertOnSubmit(newDoctor);
                db.SubmitChanges();
                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        public Doctor GetDoctorInfo(string fname, string lname)
        {
            try
            {
                Doctor result = db.Doctors.Single<Doctor>(doctor => doctor.First_name == fname && doctor.Last_name == lname);
                return result;
            }
            catch
            {
                throw new Exception("Not found");
            }
        }

        public Boolean AppointmentBooking(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime, string cname)
        {

            try
            {
                var newAppointment = new Appointment();
                try
                {
                    newAppointment.Health_Insurance_NO_ = GetPatientInfo(pfname, plname).Health_Insurance_NO_;
                }
                catch 
                {
                    throw new Exception("Patient's inforamtion is not found");
                }

                try
                {
                    newAppointment.Medical_Registration_NO_ = GetDoctorInfo(dfname, dlname).Medical_Registration_NO_;
                }
                catch
                {
                    throw new Exception("Doctor's inforamtion is not found");
                }
                
                newAppointment.Appointment_date___time = appointmentDateTime;
                newAppointment.Clinic_name = cname;
                db.Appointments.InsertOnSubmit(newAppointment);
                db.SubmitChanges();
                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        public Appointment GetAppointment(string pfname, string plname, string dfname, string dlname)
        {

            string healthInsuranceNO;
            string medicalRegistrationNO;

            try
            {
                healthInsuranceNO = GetPatientInfo(pfname, plname).Health_Insurance_NO_;
            }
            catch
            {
                throw new Exception("Patient's inforamtion is not found");
            }

            try
            {
                medicalRegistrationNO = GetDoctorInfo(dfname, dlname).Medical_Registration_NO_;
            }
            catch
            {
                throw new Exception("Doctor's inforamtion is not found");
            }

            try
            {
                Appointment result = db.Appointments.Single<Appointment>(appointment => appointment.Health_Insurance_NO_ == healthInsuranceNO
                    && appointment.Medical_Registration_NO_ == medicalRegistrationNO);
                return result;               
            }
            catch 
            {
                throw new Exception("not found");
            }
        }

        public Boolean AppointmentReschedule(string pfname, string plname, string dfname, string dlname, DateTime appointmentDateTime)
        {
            try
            {
                Appointment result = GetAppointment(pfname,plname,dfname,dlname);
                result.Appointment_date___time = appointmentDateTime;
                db.SubmitChanges();
                return true;
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }            
        }
    }
}
