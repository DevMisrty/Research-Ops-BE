package com.example.practiceddatajpa.model.enums;

public enum Status {
    SCHEDULED,

    // The appointment is booked, but confirmation is pending (e.g., waiting for patient reply).
    PENDING_CONFIRMATION,

    // The patient is currently with the doctor or waiting to be seen.
    IN_PROGRESS,

    // The appointment is complete.
    COMPLETED,

    // The patient or clinic canceled the appointment before it happened.
    CANCELED,

    // The patient did not show up for the appointment.
    NO_SHOW;
}

