package com.spotit.api.smtp.entity;

/** primary is tried first; backup is only used when sending via primary throws. */
public enum SmtpRole {
    primary, backup
}
