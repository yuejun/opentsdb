/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package net.opentsdb.tsd;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpanGroup implements org.apache.thrift.TBase<SpanGroup, SpanGroup._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SpanGroup");

  private static final org.apache.thrift.protocol.TField SPANS_FIELD_DESC = new org.apache.thrift.protocol.TField("spans", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new SpanGroupStandardSchemeFactory());
    schemes.put(TupleScheme.class, new SpanGroupTupleSchemeFactory());
  }

  public List<Span> spans; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SPANS((short)1, "spans");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SPANS
          return SPANS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SPANS, new org.apache.thrift.meta_data.FieldMetaData("spans", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Span.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SpanGroup.class, metaDataMap);
  }

  public SpanGroup() {
  }

  public SpanGroup(
    List<Span> spans)
  {
    this();
    this.spans = spans;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SpanGroup(SpanGroup other) {
    if (other.isSetSpans()) {
      List<Span> __this__spans = new ArrayList<Span>();
      for (Span other_element : other.spans) {
        __this__spans.add(new Span(other_element));
      }
      this.spans = __this__spans;
    }
  }

  public SpanGroup deepCopy() {
    return new SpanGroup(this);
  }

  @Override
  public void clear() {
    this.spans = null;
  }

  public int getSpansSize() {
    return (this.spans == null) ? 0 : this.spans.size();
  }

  public java.util.Iterator<Span> getSpansIterator() {
    return (this.spans == null) ? null : this.spans.iterator();
  }

  public void addToSpans(Span elem) {
    if (this.spans == null) {
      this.spans = new ArrayList<Span>();
    }
    this.spans.add(elem);
  }

  public List<Span> getSpans() {
    return this.spans;
  }

  public SpanGroup setSpans(List<Span> spans) {
    this.spans = spans;
    return this;
  }

  public void unsetSpans() {
    this.spans = null;
  }

  /** Returns true if field spans is set (has been assigned a value) and false otherwise */
  public boolean isSetSpans() {
    return this.spans != null;
  }

  public void setSpansIsSet(boolean value) {
    if (!value) {
      this.spans = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SPANS:
      if (value == null) {
        unsetSpans();
      } else {
        setSpans((List<Span>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SPANS:
      return getSpans();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SPANS:
      return isSetSpans();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof SpanGroup)
      return this.equals((SpanGroup)that);
    return false;
  }

  public boolean equals(SpanGroup that) {
    if (that == null)
      return false;

    boolean this_present_spans = true && this.isSetSpans();
    boolean that_present_spans = true && that.isSetSpans();
    if (this_present_spans || that_present_spans) {
      if (!(this_present_spans && that_present_spans))
        return false;
      if (!this.spans.equals(that.spans))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(SpanGroup other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    SpanGroup typedOther = (SpanGroup)other;

    lastComparison = Boolean.valueOf(isSetSpans()).compareTo(typedOther.isSetSpans());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSpans()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.spans, typedOther.spans);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SpanGroup(");
    boolean first = true;

    sb.append("spans:");
    if (this.spans == null) {
      sb.append("null");
    } else {
      sb.append(this.spans);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SpanGroupStandardSchemeFactory implements SchemeFactory {
    public SpanGroupStandardScheme getScheme() {
      return new SpanGroupStandardScheme();
    }
  }

  private static class SpanGroupStandardScheme extends StandardScheme<SpanGroup> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SpanGroup struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SPANS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list56 = iprot.readListBegin();
                struct.spans = new ArrayList<Span>(_list56.size);
                for (int _i57 = 0; _i57 < _list56.size; ++_i57)
                {
                  Span _elem58; // required
                  _elem58 = new Span();
                  _elem58.read(iprot);
                  struct.spans.add(_elem58);
                }
                iprot.readListEnd();
              }
              struct.setSpansIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, SpanGroup struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.spans != null) {
        oprot.writeFieldBegin(SPANS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.spans.size()));
          for (Span _iter59 : struct.spans)
          {
            _iter59.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SpanGroupTupleSchemeFactory implements SchemeFactory {
    public SpanGroupTupleScheme getScheme() {
      return new SpanGroupTupleScheme();
    }
  }

  private static class SpanGroupTupleScheme extends TupleScheme<SpanGroup> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SpanGroup struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetSpans()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetSpans()) {
        {
          oprot.writeI32(struct.spans.size());
          for (Span _iter60 : struct.spans)
          {
            _iter60.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SpanGroup struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list61 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.spans = new ArrayList<Span>(_list61.size);
          for (int _i62 = 0; _i62 < _list61.size; ++_i62)
          {
            Span _elem63; // required
            _elem63 = new Span();
            _elem63.read(iprot);
            struct.spans.add(_elem63);
          }
        }
        struct.setSpansIsSet(true);
      }
    }
  }

}

